package org.lab409.mapper;

import org.apache.ibatis.annotations.*;
import org.lab409.entity.*;
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
            "points, " +
            "favouriteNum, " +
            "description) VALUES (" +
            "#{resourceID}, " +
            "#{uploaderID}, " +
            "#{categoryID}, " +
            "#{resourceMajorID}," +
            "#{resourceName}," +
            "#{downloadTimes}," +
            "#{uploadTime}," +
            "#{points}," +
            "#{favouriteNum}, " +
            "#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int uploadResource(ResourceEntity resourceEntity);

    @Update("UPDATE resource SET " +
            "categoryID = #{categoryID}, " +
            "resourceMajorID = #{resourceMajorID}," +
            "resourceName = #{resourceName}," +
            "description = #{description}," +
            "points = #{points} " +
            "WHERE resourceID = #{resourceID}")
    int updateResourceMetaData(ResourceEntity resourceEntity);

    @Update("UPDATE resource SET downloadTimes = downloadTimes + 1 WHERE resourceID = #{resourceID}")
    int increaseDownloadTimes(@Param("resourceID") String resourceID);

    @Delete("DELETE FROM resource WHERE resourceID= #{resourceID}")
    int deleteResource(@Param("resourceID") String resourceID);

    @Select("SELECT * FROM resource WHERE resourceID = #{resourceID} ")
    ResourceEntity getResourceFromID(@Param("resourceID") String resourceID);

    @Insert("INSERT INTO favouriteResource(" +
            "id, " +
            "userID," +
            "resourceID) VALUES (" +
            "#{id}, " +
            "#{userID}," +
            "#{resourceID})")
    int insertIntoFavouriteResource(FavouriteResourceEntity favouriteResourceEntity);

    @Insert("INSERT IGNORE INTO downloadResource(" +
            "id, " +
            "userID," +
            "resourceID) VALUES (" +
            "#{id}," +
            "#{userID}," +
            "#{resourceID})")
    int insertIntoDownloadResource(DownloadResourceEntity downloadResourceEntity);

    @Update("UPDATE resource SET favouriteNum = favouriteNum + 1 WHERE resourceID = #{resourceID}")
    int increaseFavouriteNum(@Param("resourceID") String resourceID);

    @Update("UPDATE resource SET favouriteNum = favouriteNum - 1 WHERE resourceID = #{resourceID}")
    int decreaseFavouriteNum(@Param("resourceID") String resourceID);

    @Update("UPDATE USER SET points = points + #{delta} WHERE ID = #{userID}")
    int updateLeftPoints(@Param("delta") Integer delta, @Param("userID") Integer userID);

    @Delete("DELETE FROM favouriteResource WHERE resourceID = #{resourceID} AND userID = #{userID}")
    int deleteFavouriteResource(@Param("resourceID") String resourceID, @Param("userID") Integer userID);

    @Select("SELECT r.* FROM resource r INNER JOIN favouriteResource f ON (r.resourceID = f.resourceID AND f.userID = #{userID} )")
    //@Results({@Result(column = "r.resourceID", property = "resourceID")})
    List<ResourceEntity> getFavouriteResourceList(@Param("userID") Integer userID);

    @Select("SELECT r.* FROM resource r INNER JOIN downloadResource d ON (r.resourceID = d.resourceID AND d.userID = #{userID} )")
    List<ResourceEntity> getDownloadResourceList(@Param("userID") Integer userID);

    @Select("SELECT * FROM resource WHERE uploaderID = #{uploaderID} ")
    List<ResourceEntity> getUploadResourceList(@Param("uploaderID") Integer uploaderID);

}
