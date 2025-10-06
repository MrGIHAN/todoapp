import React from 'react';
import TaskCard from './TaskCard';

const TaskList = ({ tasks, onTaskCompleted }) => {
  if (tasks.length === 0) {
    return (
      <div className="task-list-container">
        <h2>Recent Tasks</h2>
        <div className="empty-state">
          <p>No tasks available. Create your first task!</p>
        </div>
      </div>
    );
  }

  return (
    <div className="task-list-container">
      <h2>Recent Tasks</h2>
      <div className="task-list">
        {tasks.map((task) => (
          <TaskCard
            key={task.id}
            task={task}
            onTaskCompleted={onTaskCompleted}
          />
        ))}
      </div>
    </div>
  );
};

export default TaskList;