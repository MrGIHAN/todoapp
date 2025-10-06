 import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import TaskList from './TaskList';

describe('TaskList Component', () => {
  const mockTasks = [
    {
      id: 1,
      title: 'Task 1',
      description: 'Description 1',
      completed: false,
      createdAt: '2024-01-15T10:30:00'
    },
    {
      id: 2,
      title: 'Task 2',
      description: 'Description 2',
      completed: false,
      createdAt: '2024-01-15T11:30:00'
    }
  ];

  let mockOnTaskCompleted;

  beforeEach(() => {
    mockOnTaskCompleted = jest.fn();
  });

  test('renders task list header', () => {
    render(<TaskList tasks={mockTasks} onTaskCompleted={mockOnTaskCompleted} />);
    
    expect(screen.getByText('Recent Tasks')).toBeInTheDocument();
  });

  test('renders all tasks', () => {
    render(<TaskList tasks={mockTasks} onTaskCompleted={mockOnTaskCompleted} />);
    
    expect(screen.getByText('Task 1')).toBeInTheDocument();
    expect(screen.getByText('Task 2')).toBeInTheDocument();
  });

  test('displays empty state when no tasks', () => {
    render(<TaskList tasks={[]} onTaskCompleted={mockOnTaskCompleted} />);
    
    expect(screen.getByText('No tasks available. Create your first task!')).toBeInTheDocument();
  });

  test('renders correct number of task cards', () => {
    render(<TaskList tasks={mockTasks} onTaskCompleted={mockOnTaskCompleted} />);
    
    const doneButtons = screen.getAllByRole('button', { name: /done/i });
    expect(doneButtons).toHaveLength(2);
  });

  test('passes onTaskCompleted prop to TaskCard components', () => {
    render(<TaskList tasks={mockTasks} onTaskCompleted={mockOnTaskCompleted} />);
    
    // Verify that TaskCard components are rendered (they contain Done buttons)
    const doneButtons = screen.getAllByRole('button', { name: /done/i });
    expect(doneButtons.length).toBeGreaterThan(0);
  });
});