import React, { useEffect, useState } from 'react';import { UserQueueItemView, QueueItemForm} from '../view/UserQueueItemView.jsx';
import CreateUseEffect from './CreateUseEffect.jsx';
import { useParams } from 'react-router-dom';
 

const UserQueueItemPresenter = () => {
  const { courseId } = useParams();
  const endpoint = `queue-items/${courseId}`;
  const data = CreateUseEffect({ endpoint: endpoint });

  const endpointMsg = `queue-items/messages/${courseId}`;
  const msg = CreateUseEffect({ endpoint: endpointMsg});
 
  const [loading, setLoading] = useState(true);
  const [location, setLocation] = useState('');
  const [comment, setComment] = useState('');
  const [joined, setJoined] = useState(false);
  const [error, setError] = useState(null);
  const [item, setItem] = useState(null);
  const [itemId, setItemId] = useState(0);

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
  }, [data, joined,endpoint]);

  const handleQueue = ({ action }) => {
    let userData = {
      user_id: localStorage.getItem('id'),
      course_id: courseId,
      location: location,
      active: false,
      comment: comment,
    };

    let method = 'GET';
    let endpoint;
    switch (action) {
      case 'join':
        endpoint = 'join';
        method = 'POST';
        break;

      case 'update':
        userData = {
          ...item,
          location: location,
          comment: comment,
        };
 
        endpoint = `update`;
        method = 'PUT';
        break;

      case 'delete':
        endpoint = `delete/${itemId}`;
        method = 'DELETE';
        break;

      default:
        throw new Error('Invalid action');
    }

    fetch(`http://localhost:8080/api/queue-items/${endpoint}`, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
      body: JSON.stringify(userData),
    })
      .then(response => {
        if (response.ok || response.status === 201) {
          setJoined(true);
          console.log(`User ${action}ed the queue successfully`);
        } else {
          setError(error.message);
          throw new Error(`Failed to ${action} the queue. Status: ${response.status}`);
        }
      })
      .catch(error => console.error(`Error ${action}ing the queue:`, error));
  };

  const handleDeleteQueue = ({ itemId }) => {
    setItemId(itemId);
    handleQueue({ action: 'delete' });
  };

  const handleUpdateQueue = ({ itemId, location, comment }) => {
    if (location && comment) {
      setItemId(itemId);
      setLocation(location);
      setComment(comment);
      handleQueue({ action: 'update' });
    } else {
      alert(`Please provide location and comment before ${'update'}ing the queue.`);
    }
  };

  const handleJoinQueue = () => {
    if (location && comment) {
      handleQueue({ action: 'join' });
    } else {
      alert(`Please provide location and comment before ${'join'}ing the queue.`);
    }
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
        onJoinQueue={handleJoinQueue}
        onUpdateQueue={handleUpdateQueue}  
        onDeleteQueue={handleDeleteQueue}  
        setLocation={setLocation}
        setComment={setComment}
        location={location}
        comment={comment}
        joined={joined}  
        itemId={itemId}
        message={msg}
      />
  
      <UserQueueItemView queueItems={data} />
    </>
  );
};

export default UserQueueItemPresenter;


