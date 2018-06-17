<?php
class Notification {

    // database connection and table name
    private $conn;
    private $table_name = "device_tokens";

    // object properties
    public $token;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }


    // read tokens
    function read(){

        // select all query
        $query = "SELECT token FROM device_tokens WHERE 1";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // execute query
        $stmt->execute();

        return $stmt;
    }

    // register device token
    function create(){

        if ($this->token) {
          // query to insert record
          $query = "INSERT INTO
                      " . $this->table_name . "
                  SET
                      token=:token";

          // prepare query
          $stmt = $this->conn->prepare($query);

          // sanitize
          $this->token=htmlspecialchars(strip_tags($this->token));

          // bind values
          $stmt->bindParam(":token", $this->token);

          // execute query
          if($stmt->execute()){
              return true;
          }
        }

        return false;

    }
}
