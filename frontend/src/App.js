import React, { useState, useEffect } from 'react';
import TaskForm from './components/TaskForm';
import TaskList from './components/TaskList';
import taskService from './services/taskService';

function App() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchTasks = async () => {
    try {
      setLoading(true);
      const data = await taskService.getRecentTasks();
      setTasks(data);
      setError('');
    } catch (err) {
      setError('Failed to load tasks. Please refresh the page.');
      console.error('Error fetching tasks:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const handleTaskCreated = async (taskData) => {
    try {
      await taskService.createTask(taskData);
      await fetchTasks();
    } catch (err) {
      throw err;
    }
  };

  const handleTaskCompleted = async (taskId) => {
    try {
      await taskService.markTaskAsCompleted(taskId);
      await fetchTasks();
    } catch (err) {
      console.error('Error completing task:', err);
    }
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>Todo Task Manager</h1>
      </header>
      <main className="app-main">
        {error && <div className="global-error">{error}</div>}
        <div className="content-grid">
          <TaskForm onTaskCreated={handleTaskCreated} />
          {loading ? (
            <div className="loading">Loading tasks...</div>
          ) : (
            <TaskList tasks={tasks} onTaskCompleted={handleTaskCompleted} />
          )}
        </div>
      </main>
    </div>
  );
}

export default App;