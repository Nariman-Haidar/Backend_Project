import React, { useState, useEffect } from 'react';

const CreateUseEffect = ({ endpoint }) => {
  const [data, setData] = useState([]);

  useEffect(() => {

      const token = localStorage.getItem('token');

      let eventSource = token
      ?new EventSource(`http://localhost:8080/api/${endpoint}?token=${token}`)
      :new EventSource(`http://localhost:8080/api/${endpoint}`);
     

    const handleEvent = (event) => {
      const newData = JSON.parse(event.data);
      if (!data.some((item) => item.id === newData.id)) {
        setData((prevData) => [...prevData, newData]);
      }
    };

    const handleDeleteEvent = (newData) => {
      setData((prevData) => prevData.filter((data) => newData.id !== data.id));
    };

    const handleUpdateEvent = (event) => {
      const newData = JSON.parse(event.data);
    
      setData((prevData) =>
        prevData.map((data) => (data.id === newData.id ? newData : data))
      );
    };
    
    eventSource.onmessage = (event) => {
      const newData = JSON.parse(event.data);
      if (newData.eventType === 'delete') {
        handleDeleteEvent(newData);

      } else if (newData.eventType === 'update') {
        handleUpdateEvent(event)

      } else if (newData.eventType === 'add') {
        const filteredData = data.filter((item) => item.id !== newData.id);
        setData((prevData) => [...filteredData, newData]);
      } 
      else if (newData.eventType === 'message') {
       setData([newData]);
  }
      
      else {
        handleEvent(event);
      }
    };
    
    eventSource.onerror = (error) => {
      console.log('EventSource encountered an error:', error);
      eventSource.close(); 
    };
    return () => {
      eventSource.close();
    };

  }, [endpoint, data]);
  return data;
};

export default CreateUseEffect;




 




