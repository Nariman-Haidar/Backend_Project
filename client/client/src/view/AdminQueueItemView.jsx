
    import React from 'react';
    const QueueItemForm = (props) => {
        return (
          <form>
            <label>
              New Message:
              <input
                type="text"
                value={props.newMessage}
                onChange={(e) => props.setNewMessage(e.target.value)}
              />
            </label>
            {}
            <button type="button" onClick={props.onPostMessage}>
              Post Message
            </button>
          </form>
        );
      };
    

      const AdminQueueItemView = ({ queueItems, onDelete, onChangeStatus, onAddUser, dataUser}) => {
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
                  <button onClick={() => onDelete(queueItem)}>Delete</button>
                  <button onClick={() => onChangeStatus(queueItem)}>
                    {queueItem.active ? "inactive" : "active"}
                  </button>
                </li>
              ))}
            </ul>

            <div>
              <h2>Data Users</h2>
              {Array.isArray(dataUser) && dataUser.length > 0 ? (
                <ul>
                  {dataUser.map((user, index) => (
                    <li key={`${user.id}-${index}`}>
                      User ID: {user.id}, Username: {user.username} 
                      <button onClick={() => onAddUser(user)}>Add User</button>
                    </li>
                  ))}
                </ul>
              ) : (
                <p>No data users available</p>
              )}
            </div>
          </>
        );
      };
 
    
    
    export {AdminQueueItemView, QueueItemForm};