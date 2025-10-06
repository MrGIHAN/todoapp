import React, { useState } from 'react';

const TaskForm = ({ onTaskCreated }) => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!title.trim()) {
      setError('Title is required');
      return;
    }

    try {
      await onTaskCreated({ title, description });
      setTitle('');
      setDescription('');
      setError('');
    } catch (err) {
      setError('Failed to create task. Please try again.');
    }
  };

  return (
    <div className="task-form-container">
      <h2>Create New Task</h2>
      {error && <div className="error-message">{error}</div>}
      <form onSubmit={handleSubmit} className="task-form">
        <div className="form-group">
          <label htmlFor="title">Title *</label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Enter task title"
            className="form-input"
          />
        </div>
        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Enter task description"
            className="form-textarea"
            rows="4"
          />
        </div>
        <button type="submit" className="btn-primary">
          Create Task
        </button>
      </form>
    </div>
  );
};

export default TaskForm;