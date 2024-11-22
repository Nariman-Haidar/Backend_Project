package server.mapper;

import org.springframework.stereotype.Component;
import server.dto.QueueItemDTO;
import server.model.QueueItem;

@Component
public class QueueItemMapper {

    public QueueItemDTO queueItemToQueueItemDTO(QueueItem queueItem) {
        if (queueItem == null) {
            return new QueueItemDTO();
        }
        QueueItemDTO queueItemDTO = new QueueItemDTO();
        queueItemDTO.setId(queueItem.getId());
        queueItemDTO.setUser_id(queueItem.getUser_id());
        queueItemDTO.setCourse_id(queueItem.getCourse_id());
        queueItemDTO.setLocation(queueItem.getLocation());
        queueItemDTO.setActive(queueItem.isActive());
        queueItemDTO.setComment(queueItem.getComment());

        return queueItemDTO;
    }

    public QueueItem queueItemDTOToQueueItem(QueueItemDTO queueItemDTO) {
        if (queueItemDTO == null) {
            return new QueueItem();
        }
        QueueItem queueItem = new QueueItem();
        queueItem.setId(queueItemDTO.getId());
        queueItem.setUser_id(queueItemDTO.getUser_id());
        queueItem.setCourse_id(queueItemDTO.getCourse_id());
        queueItem.setLocation(queueItemDTO.getLocation());
        queueItem.setActive(queueItemDTO.isActive());
        queueItem.setComment(queueItemDTO.getComment());

        return queueItem;
    }
}


