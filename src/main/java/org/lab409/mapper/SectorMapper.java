package org.lab409.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.lab409.entity.Sector;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jiao on 2018/12/9.
 */
@Mapper
@Repository
public interface SectorMapper {

    //get all sectors
    @Select("SELECT a.SectorId,a.SectorName FROM forum_sector a")
    List<Sector> getSectors();
}
