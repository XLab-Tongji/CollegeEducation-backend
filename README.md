# CollegeEducation-backend

Dependency：

ElasticSearch 6.4.2 

IkAnalyzer 6.4.2


ElasticSearch config 

This simple config is for testing on my mac 

You need to change the cluster-nodes address and the cluster-name according to your pc/server version 

If you want to test on your pc, on mac, use HomeBrew ("brew install elasticsearch") to install(by default it is the latest version 6.4.2 which works on my mac), add its bin to your path. type "elasticsearch" in terminal to launch it  

You can use curl ip:9200  or just type 127.0.0.1:9200 in your browser to get the default settings of your elasticsearch(cluster-name is the most important, it has to be matched to your node ip) 

I believe windows users will follow almost the same tutorial to make it work. 

I have found docker version of elasticsearch on 10.60.38.173 server but it seems to be a older version(2.4...)  

If you are going to use IKAnalyzer, you can download IkAnalyzer plugin hear https://github.com/medcl/elasticsearch-analysis-ik/releases and copy it to your config directory  

If any of the existing index conflict due to the change of "resource_search.json" or other index config(index mapping can not be change...) in resource directory, the simplest way is to use "curl -XDELETE http://localhost:9200/{index name}/{type name}" to delete all the existing index and restart the app.
