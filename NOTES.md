# Scala 

- **[Naming convention](https://docs.scala-lang.org/style/naming-conventions.html)**



# Elasticsearch and Kibana
 
Play with ES using Kibana console or other REST client

- **[elastic4s - Elasticsearch Scala Client](https://github.com/sksamuel/elastic4s)**
- **[elastic4s - Elasticsearch Scala Client (Scala org)](https://index.scala-lang.org/sksamuel/elastic4s/elastic4s-core)**
- **[Boolean query](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-bool-query.html)**
- **[]()**
- **[]()**

```
# Search Queries with _search API 

GET _search
{
  "query": {
    "match_all": {}
  }
}

GET recipes/_search
{
  "query": {
    "match_all": {}
  }
}

GET recipes/_search
{
  "query": {
    "match": {
      "id": "1"
    }
  }
}

GET recipes/_search
{
  "query": {
    "multi_match": {
      "query": "rolls"
    }
  },
  "explain": true
}

GET recipes/_search
{
  "query": {
    "bool": {
      "must": {
        "term" : { "tags" : "tag1" }
      }
    }
  }
}

GET recipes/_search
{
  "query": {
    "bool": {
      "should": {
        "term" : { "tags" : "tag1" }
      }
    }
  }
}


GET /_search
{
  "query": {
    "dis_max": {
      "queries": [
        { "match": { "ingredients": "sugar" }},
        { "match": { "tags": "butter" }}
      ]
    }
  }
}

DELETE recipes


# _doc API

GET recipes/_doc/1

GET recipes/_doc/3acf0bc4-c183-4430-872d-6a90fbf96717

DELETE recipes/_doc/3acf0bc4-c183-4430-872d-6a90fbf96717


# Update API

POST recipes/_update/3acf0bc4-c183-4430-872d-6a90fbf96717
{
  "script": {
    "source": "ctx._source.tags.addAll(params.newTags)",
    "params": {
      "newTags": ["Update tag3", "Another new tag4"]
    }
  }
}

# Mapping API

GET recipes/_mapping/field/name
```

# ScalaJS and ScalaReact with Diode

- **[scalajs-react](https://github.com/japgolly/scalajs-react)**
- **[Diode state management](https://github.com/suzaku-io/diode)**
- **[]()**
- **[]()**

# React 

- **[Thinking in React](https://reactjs.org/docs/thinking-in-react.html)**
- **[]()**