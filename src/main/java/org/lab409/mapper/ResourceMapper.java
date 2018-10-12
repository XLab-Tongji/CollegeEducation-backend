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
            "description) VALUES (" +
            "#{resourceID}, " +
            "#{uploaderID}, " +
            "#{categoryID}, " +
            "#{resourceMajorID}," +
            "#{resourceName}," +
            "#{description})")
    int uploadResource(ResourceEntity resourceEntity);

    @Update("UPDATE resource SET " +
            "categoryID = #{categoryID}, " +
            "resourceMajorID = #{resourceMajorID}," +
            "resourceName = #{resourceName}," +
            "description = #{description} " +
            "WHERE resourceID = #{resourceID}")
    int updateResourceMetaData(ResourceEntity resourceEntity);

    @Delete("DELETE FROM resource WHERE resourceID=#{resourceID}")
    int deleteResource(@Param("resourceID") String resourceID);

}
