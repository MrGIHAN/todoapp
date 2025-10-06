import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import TaskCard from './TaskCard';

describe('TaskCard Component', () => {
  const mockTask = {
    id: 1,
    title: 'Test Task',
    description: 'Test Description',
    completed: false,
    createdAt: '2024-01-15T10:30:00'
  };

  let mockOnTaskCompleted;

  beforeEach(() => {
    mockOnTaskCompleted = jest.fn();
  });

  test('renders task card with all information', () => {
    render(<TaskCard task={mockTask} onTaskCompleted={mockOnTaskCompleted} />);
    
    expect(screen.getByText('Test Task')).toBeInTheDocument();
    expect(screen.getByText('Test Description')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /done/i })).toBeInTheDocument();
  });

  test('displays "No description provided" when description is empty', () => {
    const taskWithoutDescription = { ...mockTask, description: '' };
    render(<TaskCard task={taskWithoutDescription} onTaskCompleted={mockOnTaskCompleted} />);
    
    expect(screen.getByText('No description provided')).toBeInTheDocument();
  });

  test('displays "No description provided" when description is null', () => {
    const taskWithoutDescription = { ...mockTask, description: null };
    render(<TaskCard task={taskWithoutDescription} onTaskCompleted={mockOnTaskCompleted} />);
    
    expect(screen.getByText('No description provided')).toBeInTheDocument();
  });

  test('formats date correctly', () => {
    render(<TaskCard task={mockTask} onTaskCompleted={mockOnTaskCompleted} />);
    
    // The date format will depend on locale, but it should contain some part of the date
    const dateElement = screen.getByText(/Jan|15|2024/i);
    expect(dateElement).toBeInTheDocument();
  });

  test('calls onTaskCompleted when Done button is clicked', async () => {
    mockOnTaskCompleted.mockResolvedValue({});
    render(<TaskCard task={mockTask} onTaskCompleted={mockOnTaskCompleted} />);
    
    const doneButton = screen.getByRole('button', { name: /done/i });
    fireEvent.click(doneButton);

    expect(mockOnTaskCompleted).toHaveBeenCalledWith(1);
  });

  test('handles long title text', () => {
    const longTitleTask = {
      ...mockTask,
      title: 'This is a very long task title that should wrap properly without breaking the layout'
    };
    
    render(<TaskCard task={longTitleTask} onTaskCompleted={mockOnTaskCompleted} />);
    
    expect(screen.getByText(longTitleTask.title)).toBeInTheDocument();
  });

  test('handles long description text', () => {
    const longDescTask = {
      ...mockTask,
      description: 'This is a very long description that contains multiple sentences and should wrap properly within the card without breaking the layout or causing overflow issues.'
    };
    
    render(<TaskCard task={longDescTask} onTaskCompleted={mockOnTaskCompleted} />);
    
    expect(screen.getByText(longDescTask.description)).toBeInTheDocument();
  });
});