  import React from 'react';

  const QueueItemForm = (props) => {
    return (
      <form>
        <label>
          Location:
          <input type="text" value={props.location} onChange={(e) => props.setLocation(e.target.value)} />
        </label>
        <label>
          Comment:
          <input type="text" value={props.comment} onChange={(e) => props.setComment(e.target.value)} />
        </label>
        {}
        {!props.joined && (
          <button type="button" onClick={() => props.onJoinQueue({ location: props.location, comment: props.comment })}>
            Join Queue
          </button>
        )}
        {}
        {props.joined && props.onUpdateQueue && props.onDeleteQueue && (
          <>
            <button type="button" onClick={() => props.onUpdateQueue({ itemId: props.itemId, location: props.location, comment: props.comment })}>
              Update Queue
            </button>
            <button type="button" onClick={() => props.onDeleteQueue({ itemId: props.itemId })}>
              Delete Queue
            </button>
          </>
        )}
        {}
        {props.message && props.message.map((message, index) => (
          <div key={index}>Message: {message.message}</div>
        ))}
      </form>
    );
  };
  
  export default QueueItemForm;
  


const UserQueueItemView = ({ queueItems }) => {
  if (!Array.isArray(queueItems) || queueItems.length === 0) {
    return (
      <>
        <p>No data available</p>
      </>
    );
  }

  return (
    <>

      <ul>
        {queueItems.map((queueItem, index) => (
          <li key={`${queueItem.id}-${index}`}>
            User ID: {queueItem.user_id}, Course ID: {queueItem.course_id}, 
            Location: {queueItem.location}, Active: {queueItem.active ? "YES" : "NO"}, 
            Comment: {queueItem.comment}
          </li>
        ))}
      </ul>
    </>
  );
};

export { UserQueueItemView, QueueItemForm };




