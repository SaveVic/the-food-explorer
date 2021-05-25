## Search Food by Query
* Endpoint: `/api/v1/food?q={query}`
* HTTP Method: `GET`
* Request Header:
    * Content-type: `application/json`
* Response:
    ```JSON
      {
        "success": true,
        "message": "Some message",
        "data": [
           {
              "id": 0,
              "name": "Tahu",
              "city": "Jakarta",
              "image": "http://image.com/tahu-jakarta"
           },
           {
              "id": 0,
              "name": "Tahu",
              "city": "Jakarta",
              "image": "http://image.com/tahu-jakarta"
           }
        ]
      }
    ```
  
## Search Food by Image
* Endpoint: `/api/v1/food`
* HTTP Method: `POST`
* Request Header:
    * Content-type: `application/json`
* Request MultiPart:
    * image: `jpg/jpeg/png`
* Response:
    ```JSON
      {
        "success": true,
        "message": "Some message",
        "data": [
           {
              "id": 0,
              "name": "Tahu",
              "city": "Jakarta",
              "image": "http://image.com/tahu-jakarta"
           },
           {
              "id": 0,
              "name": "Tahu",
              "city": "Jakarta",
              "image": "http://image.com/tahu-jakarta"
           }
        ]
      }
    ```
  
## Detail Food
* Endpoint: `/api/v1/food/{food_id}/detail`
* HTTP Method: `GET`
* Request Header:
    * Content-type: `application/json`
* Response:
    ```JSON
      {
        "success": true,
        "message": "Some message",
        "data": {
           "description": "Lorem ipsum",
           "ingredient": ["Nasi", "Tahu"],
           "taste": ["Pedas"]
        }
      }
    ```
  
## Location Food
* Endpoint: `/api/v1/food/{food_id}/location`
* HTTP Method: `GET`
* Request Header:
    * Content-type: `application/json`
* Response:
    ```JSON
      {
        "success": true,
        "message": "Some message",
        "data": [
           {
              "name": "Warung Muthu",
              "address": "Kampung Durian Runtuh",
              "map_url": "https://www.google.com/maps/search/gmaps/@lat,long"
           },
           {
              "name": "Warung Muthu",
              "address": "Kampung Durian Runtuh",
              "map_url": "https://www.google.com/maps/search/gmaps/@lat,long"
           }
        ]
      }
    ```