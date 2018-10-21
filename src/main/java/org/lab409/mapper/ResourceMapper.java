package org.lab409.mapper;

import org.apache.ibatis.annotations.*;
import org.lab409.entity.ResourceCategoryEntity;
import org.lab409.entity.ResourceEntity;
import org.lab409.entity.ResourceMajorEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResourceMapper {

    @Select("SELECT * FROM category")
    @Results({@Result(column = "id", property = "id"),
            @Result(column = "categoryName", property = "resourceCategoryName")})
    List<ResourceCategoryEntity> getResourceCategories();

    @Select("SELECT * FROM resourceMajor")
    List<ResourceMajorEntity> getResourceMajors();

    @Insert("INSERT IGNORE INTO resource (" +
            "resourceID, " +
            "uploaderID," +
            "categoryID, " +
            "resourceMajorID," +
            "resourceName," +
            "downloadTimes," +
            "uploadTime," +
            "description) VALUES (" +
            "#{resourceID}, " +
            "#{uploaderID}, " +
            "#{categoryID}, " +
            "#{resourceMajorID}," +
            "#{resourceName}," +
            "#{downloadTimes}," +
            "#{uploadTime}," +
            "#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int uploadResource(ResourceEntity resourceEntity);

    @Update("UPDATE resource SET " +
            "categoryID = #{categoryID}, " +
            "resourceMajorID = #{resourceMajorID}," +
            "resourceName = #{resourceName}," +
            "description = #{description} " +
            "WHERE resourceID = #{resourceID}")
    int updateResourceMetaData(ResourceEntity resourceEntity);

    @Update("UPDATE resource SET downloadTimes = downloadTimes + 1 WHERE resourceID = #{resourceID}")
    int increaseDownloadTimes(@Param("resourceID") String resourceID);

    @Delete("DELETE FROM resource WHERE resourceID=#{resourceID}")
    int deleteResource(@Param("resourceID") String resourceID);

    @Select("SELECT * FROM resource WHERE resourceID = #{resourceID} ")
    ResourceEntity getResourceFromID(@Param("resourceID") String resourceID);
}
