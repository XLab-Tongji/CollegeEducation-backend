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
            "uploadTime," +
            "points, " +
            "tags," +
            "description) VALUES (" +
            "#{resourceID}, " +
            "#{uploaderID}, " +
            "#{categoryID}, " +
            "#{resourceMajorID}," +
            "#{resourceName}," +
            "#{uploadTime}," +
            "#{points}," +
            "#{description}," +
            "#{tags})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int uploadResource(ResourceEntity resourceEntity);

    @Update("UPDATE resource SET " +
            "categoryID = #{categoryID}, " +
            "resourceMajorID = #{resourceMajorID}," +
            "resourceName = #{resourceName}," +
            "description = #{description}," +
            "points = #{points}," +
            "tags = #{tags}" +
            "WHERE resourceID = #{resourceID}")
    int updateResourceMetaData(ResourceEntity resourceEntity);

    @Delete("DELETE FROM resource WHERE resourceID= #{resourceID}")
    int deleteResource(@Param("resourceID") String resourceID);

    @Select("SELECT * FROM resource WHERE resourceID = #{resourceID} ")
    ResourceEntity getResourceFromID(@Param("resourceID") String resourceID);

    @Insert("INSERT IGNORE INTO favouriteResource(" +
            "id, " +
            "userID," +
            "resourceID," +
            "favouriteTime) VALUES (" +
            "#{id}, " +
            "#{userID}," +
            "#{resourceID}," +
            "#{favouriteTime})")
    int insertIntoFavouriteResource(FavouriteResourceEntity favouriteResourceEntity);

    @Insert("INSERT INTO downloadResource(" +
            "id, " +
            "userID," +
            "resourceID," +
            "downloadTime) VALUES (" +
            "#{id}," +
            "#{userID}," +
            "#{resourceID}," +
            "#{downloadTime})" +
            "ON DUPlICATE KEY UPDATE downloadTimes = downloadTimes + 1")
    int insertIntoDownloadResource(DownloadResourceEntity downloadResourceEntity);

    @Insert("INSERT IGNORE INTO suggestedResource(" +
            "id, " +
            "userID," +
            "resourceID," +
            "suggestTime," +
            "suggested) VALUES (" +
            "#{id}, " +
            "#{userID}," +
            "#{resourceID}," +
            "#{suggestTime}," +
            "#{suggested})" +
            "ON DUPLICATE KEY UPDATE suggested = #{suggested}")
    int insertIntoSuggestedResourceAndSuggest(SuggestedResource suggestedResourceEntity);

    @Insert("INSERT INTO resourceComment(" +
            "userID," +
            "resourceID," +
            "comment," +
            "commentTime," +
            "commentTitle," +
            "score) VALUES (" +
            "#{userID}," +
            "#{resourceID}," +
            "#{comment}," +
            "#{commentTime}," +
            "#{commentTitle}," +
            "#{score})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertIntoResourceComment(ResourceComment resourceComment);

    @Update("UPDATE USER SET points = points + #{delta} WHERE ID = #{userID}")
    int updateLeftPoints(@Param("delta") Integer delta, @Param("userID") Integer userID);

    @Delete("DELETE FROM favouriteResource WHERE resourceID = #{resourceID} AND userID = #{userID}")
    int deleteFavouriteResource(@Param("resourceID") String resourceID, @Param("userID") Integer userID);

    @Delete("DELETE FROM suggestedResource WHERE resourceID = #{resourceID} AND userID = #{userID}")
    int deleteSuggestedResource(@Param("resourceID") String resourceID, @Param("userID") Integer userID);

    @Delete("DELETE FROM resourceComment WHERE id = #{commentID} AND userID = #{userID}")
    int deleteResourceComment(@Param("commentID") Integer commentID, @Param("userID") Integer userID);

    @Select("SELECT r.* FROM resource r INNER JOIN favouriteResource f ON (r.resourceID = f.resourceID AND f.userID = #{userID} ) ORDER BY f.favouriteTime DESC")
    List<ResourceEntity> getFavouriteResourceList(@Param("userID") Integer userID);

    @Select("SELECT r.* FROM resource r INNER JOIN downloadResource d ON (r.resourceID = d.resourceID AND d.userID = #{userID} ) ORDER BY d.downloadTime DESC")
    List<ResourceEntity> getDownloadResourceList(@Param("userID") Integer userID);

    @Select("SELECT r.* FROM resource r INNER JOIN suggestedResource s ON (r.resourceID = s.resourceID AND s.userID = #{userID} ) ORDER BY s.suggestTime DESC")
    List<ResourceEntity> getSuggestedResourceList(@Param("userID") Integer userID);

    @Select("SELECT * FROM resourceComment WHERE resourceID = #{resourceID} ORDER BY commentTime DESC")
    List<ResourceComment> getResourceCommentList(@Param("resourceID") String resourceID);

    @Select("SELECT * FROM resourceComment WHERE userID = #{userID} ORDER BY commentTime DESC")
    List<ResourceComment> getMyResourceCommentList(@Param("userID") Integer userID);

    @Select("SELECT r.* FROM resource r INNER JOIN resourceComment c ON (r.resourceID = c.resourceID AND c.userID = #{userID} )")
    List<ResourceEntity> getCommentResourceList(@Param("userID") Integer userID);

    @Select("SELECT * FROM resource WHERE uploaderID = #{uploaderID} ORDER BY uploadTime DESC")
    List<ResourceEntity> getUploadResourceList(@Param("uploaderID") Integer uploaderID);

    //暂时这么写
    @Select("SELECT r.* FROM (SELECT * FROM resource WHERE categoryID = #{categoryID} AND resourceMajorID = #{resourceMajorID}) AS r " +
            "INNER JOIN (SELECT resourceID, AVG(score) as avgScore FROM resourceComment " +
            "GROUP BY resourceID ) AS a ON r.resourceID = a.resourceID ORDER BY a.avgScore DESC")
    List<ResourceEntity> getRecommendResourceList(@Param("categoryID") Integer categoryID, @Param("resourceMajorID") Integer resourceMajorID);

    /*@Select("SELECT r.* FROM resource r WHERE r.resourceID in ${keywordEntity}" +
            "INNER JOIN (SELECT resourceID, AVG(r1.score) as avgScore FROM resourceComment" +
            "GROUP BY resourceID) AS a ON r.resourceID = a.resourceID ORDER BY a.score DESC")
    List<ResourceEntity> getKeyWordResourceOrderByTime(@Param("keywordEntity") ResourceEntity resourceEntity);

    @Select()
    List<ResourceEntity> getKeyWordResourceOrderByScore(@Param("keywordEntity") ResourceEntity resourceEntity);*/

    @Select("SELECT COUNT(*) FROM (SELECT id FROM downloadResource WHERE resourceID = #{resourceID}) as d")
    Integer getDownloadTimes(@Param("resourceID") String resourceID);

    @Select("SELECT COUNT(*) FROM (SELECT id FROM favouriteResource WHERE resourceID = #{resourceID}) as f")
    Integer getFavouriteNum(@Param("resourceID") String resourceID);

    @Select("SELECT r.uploaderID, u.USERNAME " +
            "FROM resource r " +
            "INNER JOIN USER u on (r.uploaderID = u.ID AND r.resourceID = #{resourceID})")
    @Results({@Result(column = "uploaderID", property = "uploaderID"),
            @Result(column = "USERNAME", property = "uploaderName")})
    ResourceDetail.UploaderInfo getResourceUploaderInfo(@Param("resourceID") String resourceID);

    @Select("SELECT COUNT(*) as commentNum, AVG(c.score) as avgScore " +
            "FROM (SELECT score FROM resourceComment WHERE resourceID = #{resourceID}) AS c")
    @Results({@Result(column = "commentNum", property = "commentNum"),
            @Result(column = "avgScore", property = "resourceAvgScore")})
    ResourceDetail.CommentInfo getResourceCommentInfo(@Param("resourceID") String resourceID);

    @Select("SELECT SUM(s.suggested) as totalSuggested, SUM(s.suggested)/COUNT(*) as suggestedRate " +
            "FROM (SELECT suggested FROM suggestedResource WHERE resourceID = #{resourceID}) AS s")
    @Results({@Result(column = "totalSuggested", property = "suggestedNum"),
            @Result(column = "suggestedRate", property = "resourceSuggestedRate")})
    ResourceDetail.SuggestInfo getResourceSuggestedInfo(@Param("resourceID") String resourceID);

    @Select("SELECT COUNT(*) FROM (SELECT id FROM favouriteResource WHERE userID = #{userID}) as f")
    Integer getMyFavouriteNum(@Param("userID") Integer userID);

    @Select("SELECT COUNT(*) FROM (SELECT id FROM suggestedResource WHERE userID = #{userID}) as s")
    Integer getMySuggestedNum(@Param("userID") Integer userID);

    @Select("SELECT COUNT(*) FROM (SELECT id FROM downloadResource WHERE userID = #{userID}) as d")
    Integer getMyDownloadNum(@Param("userID") Integer userID);

    @Select("SELECT COUNT(*) FROM (SELECT id FROM resource WHERE uploaderID = #{userID}) as r")
    Integer getMyUploadNum(@Param("userID") Integer userID);

    @Select("SELECT AVG(score) FROM (SELECT score FROM resourceComment WHERE userID = #{userID}) as s")
    Double getMyResourceAvgScore(@Param("userID") Integer userID);

    @Select("SELECT SUM(suggested)/COUNT(*) FROM (SELECT suggested FROM suggestedResource INNER JOIN " +
            "resource WHERE uploaderID = #{userID}) as s")
    Double getMyResourceSuggestedRate(@Param("userID") Integer userID);

    @Select("SELECT EXISTS (SELECT * FROM favouriteResource WHERE resourceID = #{resourceID} AND userID = #{userID})")
    Integer isUserFavouriteResource(@Param("resourceID") String resourceID, @Param("userID") Integer userID);

    @Select("SELECT suggested FROM suggestedResource WHERE resourceID = #{resourceID} AND userID = #{userID}")
    @Results({@Result(column = "suggested", property = "suggested")})
    SuggestedResource isUserSuggestedResource(@Param("resourceID") String resourceID, @Param("userID") Integer userID);
}
