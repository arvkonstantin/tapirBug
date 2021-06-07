1) run Main.scala
2) open http://localhost:8081/api/
3) default request:
   {
       "data": {
         "value": "string"
     }
   }
   don't work
   
4) this:
   {
       "data": {
            "Left": {
                "value": "string"
            }
    }
   }
   
works fine.

but in yaml file, we don't see anything about "Left"
