import React from 'react';

const TaskCard = ({ task, onTaskCompleted }) => {
  const handleComplete = async () => {
    try {
      await onTaskCompleted(task.id);
    } catch (error) {
      console.error('Error completing task:', error);
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  return (
    <div className="task-card">
      <div className="task-card-header">
        <h3 className="task-title">{task.title}</h3>
        <span className="task-date">{formatDate(task.createdAt)}</span>
      </div>
      <p className="task-description">{task.description || 'No description provided'}</p>
      <div className="task-card-footer">
        <button onClick={handleComplete} className="btn-done">
          Done
        </button>
      </div>
    </div>
  );
};

export default TaskCard;