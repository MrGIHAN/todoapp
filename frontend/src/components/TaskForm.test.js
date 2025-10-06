import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import TaskForm from './TaskForm';

describe('TaskForm Component', () => {
  let mockOnTaskCreated;

  beforeEach(() => {
    mockOnTaskCreated = jest.fn();
  });

  test('renders task form with all fields', () => {
    render(<TaskForm onTaskCreated={mockOnTaskCreated} />);
    
    expect(screen.getByText('Create New Task')).toBeInTheDocument();
    expect(screen.getByLabelText(/title/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/description/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /create task/i })).toBeInTheDocument();
  });

  test('shows error when submitting empty title', async () => {
    render(<TaskForm onTaskCreated={mockOnTaskCreated} />);
    
    const submitButton = screen.getByRole('button', { name: /create task/i });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('Title is required')).toBeInTheDocument();
    });
    
    expect(mockOnTaskCreated).not.toHaveBeenCalled();
  });

  test('calls onTaskCreated with correct data when form is submitted', async () => {
    mockOnTaskCreated.mockResolvedValue({});
    render(<TaskForm onTaskCreated={mockOnTaskCreated} />);
    
    const titleInput = screen.getByLabelText(/title/i);
    const descriptionInput = screen.getByLabelText(/description/i);
    const submitButton = screen.getByRole('button', { name: /create task/i });

    fireEvent.change(titleInput, { target: { value: 'Test Task' } });
    fireEvent.change(descriptionInput, { target: { value: 'Test Description' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(mockOnTaskCreated).toHaveBeenCalledWith({
        title: 'Test Task',
        description: 'Test Description'
      });
    });
  });

  test('clears form after successful submission', async () => {
    mockOnTaskCreated.mockResolvedValue({});
    render(<TaskForm onTaskCreated={mockOnTaskCreated} />);
    
    const titleInput = screen.getByLabelText(/title/i);
    const descriptionInput = screen.getByLabelText(/description/i);
    const submitButton = screen.getByRole('button', { name: /create task/i });

    fireEvent.change(titleInput, { target: { value: 'Test Task' } });
    fireEvent.change(descriptionInput, { target: { value: 'Test Description' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(titleInput.value).toBe('');
      expect(descriptionInput.value).toBe('');
    });
  });

  test('displays error message when task creation fails', async () => {
    mockOnTaskCreated.mockRejectedValue(new Error('API Error'));
    render(<TaskForm onTaskCreated={mockOnTaskCreated} />);
    
    const titleInput = screen.getByLabelText(/title/i);
    const submitButton = screen.getByRole('button', { name: /create task/i });

    fireEvent.change(titleInput, { target: { value: 'Test Task' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('Failed to create task. Please try again.')).toBeInTheDocument();
    });
  });

  test('trims whitespace from title before validation', async () => {
    render(<TaskForm onTaskCreated={mockOnTaskCreated} />);
    
    const titleInput = screen.getByLabelText(/title/i);
    const submitButton = screen.getByRole('button', { name: /create task/i });

    fireEvent.change(titleInput, { target: { value: '   ' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('Title is required')).toBeInTheDocument();
    });
    
    expect(mockOnTaskCreated).not.toHaveBeenCalled();
  });
});