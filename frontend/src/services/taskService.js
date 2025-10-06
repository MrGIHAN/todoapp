import axios from 'axios';

const API_URL = 'http://localhost:8080/api/tasks';

const taskService = {
  createTask: async (taskData) => {
    try {
      const response = await axios.post(`${API_URL}/create`, taskData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getRecentTasks: async () => {
    try {
      const response = await axios.get(`${API_URL}/gettask`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  markTaskAsCompleted: async (taskId) => {
    try {
      const response = await axios.put(`${API_URL}/${taskId}/complete`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  deleteTask: async (taskId) => {
    try {
      await axios.delete(`${API_URL}/${taskId}`);
    } catch (error) {
      throw error;
    }
  }
};

export default taskService;