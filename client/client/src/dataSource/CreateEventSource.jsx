
const CreateEventSource = (endpoint, callback) => {
  const eventSource = new EventSource(`http://localhost:8080/api/${endpoint}`);

  eventSource.onmessage = (event) => {
      const newData = JSON.parse(event.data);
      callback(newData);
  };

  eventSource.onerror = (error) => {
    console.log('EventSource encountered an error:', error);
    eventSource.close(); 
  };

  return eventSource;
};

export default CreateEventSource;

  