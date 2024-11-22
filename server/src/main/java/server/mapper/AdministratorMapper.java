package server.mapper;


import org.springframework.stereotype.Component;
import server.dto.AdministratorDTO;
import server.model.Administrator;


@Component
public class AdministratorMapper {

    public AdministratorDTO administratorToAdministratorDTO(Administrator administrator) {
        if (administrator == null) {
            return new AdministratorDTO();
        }

        AdministratorDTO administratorDTO = new AdministratorDTO();
        administratorDTO.setCourseId(administrator.getCourseId());
        administratorDTO.setUserId(administrator.getUserId());
        return administratorDTO;
    }
}









