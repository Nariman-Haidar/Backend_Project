import React, { useEffect, useState } from 'react';
import { AdminQueueItemView , QueueItemForm} from '../view/AdminQueueItemView.jsx';
import CreateUseEffect from './CreateUseEffect.jsx';
import { useParams } from 'react-router-dom';

const AdminQueueItemPresenter = () => {
  const { courseId } = useParams();
  const endpoint = `queue-items/${courseId}`;
  const data = CreateUseEffect({ endpoint: endpoint });

  const endpoint2 = `access/by-course/${courseId}`;
  const dataUser = CreateUseEffect({ endpoint: endpoint2 });

  const [loading, setLoading] = useState(true);
  const [location, setLocation] = useState('');
  const [comment, setComment] = useState('');
  const [joined, setJoined] = useState(false);
  const [error, setError] = useState(null);
  const [item, setItem] = useState(null);
  const [itemId, setItemId] = useState(0);
  const [newMessage, setNewMessage] = useState('');
  useEffect(() => {
    if (data) {
      const userId = localStorage.getItem('id');
      const joinedItem = data.find((item) => item.user_id == userId);

      if (joinedItem) {
        setItemId(joinedItem.id);
        setItem(joinedItem);
        setJoined(true);
      } else {
        setJoined(false);
        setItem(null);

      }
    }
    setLoading(false);
  }, [data, joined, endpoint]);


  const handleDelete = (queueItem) => {
    fetch(`http://localhost:8080/api/queue-items/delete/${queueItem.id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
      body: JSON.stringify(queueItem),
    })
      .then(response => {
        if (response.ok || response.status === 201) {
        } else {
          setError(error.message);
          throw new Error(`Failed to delete the queue. Status: ${response.status}`);
        }
      })
      .catch(error => console.error(`Error deleting the queue:`, error));
  };
  
  const handleChangeStatus = (queueItem) => {
    fetch(`http://localhost:8080/api/queue-items/update`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
      body: JSON.stringify(
        {
          ...queueItem,
          active: queueItem.active? false: true
        }),
    })
      .then(response => {
        if (response.ok || response.status === 201) {
        } else {
          setError(error.message);
          throw new Error(`Failed toupdate the queue. Status: ${response.status}`);
        }
      })
      .catch(error => console.error(`Error updating the queue:`, error));
  };
  
 
const handlePostMessage = () => {
  fetch(`http://localhost:8080/api/queue-items/message/${courseId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`,
    },
    body: JSON.stringify({ message: newMessage }),
  })
    .then(response => {
      if (response.ok || response.status === 201) {
      } else {
        setError(error.message);
        throw new Error(`Failed to post message. Status: ${response.status}`);
      }
    })
    .catch(error => console.error(`Error posting message:`, error));

  setNewMessage('');
};

const handleAddUser = (user) => {
  fetch(`http://localhost:8080/api/queue-items/join`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`,
    },
    body: JSON.stringify(
      {
        user_id:user.id,
        course_id:courseId,
        location:"",
        active:false,
        comment:""
      } )
  })
    .then(response => {
      if (response.ok || response.status === 201) {
      } else {
        setError(error.message);
        throw new Error(`Failed toupdate the queue. Status: ${response.status}`);
      }
    })
    .catch(error => console.error(`Error updating the queue:`, error));
  console.log('Adding a user to the course...');
};
   
  if (loading) {
    return <p>Loading...</p>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <>
      <QueueItemForm
        onPostMessage={handlePostMessage} 
        newMessage={newMessage} 
        setNewMessage={setNewMessage} 
      />
      <AdminQueueItemView
        queueItems={data}
        onDelete={handleDelete}
        onChangeStatus={handleChangeStatus}
        onAddUser={handleAddUser} 
        dataUser={dataUser} 
      />
    </>
  );
};

export default AdminQueueItemPresenter;
 