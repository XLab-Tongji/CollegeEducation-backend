package org.lab409.mapper;

import org.apache.ibatis.annotations.*;
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

    //add association between sectorName and Topic to 'forum_sector_use' table
    @Insert("INSERT INTO forum_sector_use (topic_id,sector_id) VALUES(#{topic_id},#{sector_id}) ")
    int addAssociation(@Param(value = "topic_id")Integer topic_id,@Param(value = "sector_id")Integer sector_id);

    //add association between sectorName and Draft to 'forum_sector_use_draft' table
    @Insert("INSERT INTO forum_sector_use_draft (draft_id,sector_id) VALUES(#{draft_id},#{sector_id}) ")
    int addAssociationToDraft(@Param(value = "draft_id")Integer draft_id,@Param(value = "sector_id")Integer sector_id);

    //get SectorId by SectorName
    @Select("SELECT IFNULL((SELECT a.SectorId FROM forum_sector a WHERE a.SectorName=#{SectorName}),-1)")
    int getSectorIdBySectorName(@Param(value = "SectorName")String SectorName);

    //save new Sector
    @Insert("INSERT INTO forum_sector (SectorName) VALUES(#{SectorName})")
    @Options(useGeneratedKeys = true,keyProperty = "SectorId",keyColumn = "SectorId")
    int addNewSector(Sector sector);
}
