package server.mapper;


import org.springframework.stereotype.Component;
import server.dto.AccessDTO;
import server.model.Access;
@Component
public class AccessMapper {

    public AccessDTO accessToAccessDTO(Access access) {
        if (access == null) {
            return null;
        }

        AccessDTO accessDTO = new AccessDTO();
        accessDTO.setUserId(access.getUserId());
        accessDTO.setCourseId(access.getCourseId());

        return accessDTO;
    }

    public Access accessDTOToAccess(AccessDTO accessDTO) {
        if (accessDTO == null) {
            return null;
        }

        Access access = new Access();

        access.setUserId(accessDTO.getUserId());
        access.setCourseId(accessDTO.getCourseId());

        return access;
    }
}
