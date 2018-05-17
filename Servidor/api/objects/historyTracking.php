<?php
class HistoryTracking {

    // database connection and table name
    private $conn;
    private $table_name = "historicotracking";

    // object properties
    public $area;
    public $duracao;
    public $trackingId;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read history
    function read(){

        // select all query
        $query = "SELECT * FROM historicotracking WHERE trackingId = :trackingId";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->trackingId=htmlspecialchars(strip_tags($this->trackingId));

        // bind values
        $stmt->bindParam(":trackingId", $this->trackingId);

        // execute query
        $stmt->execute();

        return $stmt;
    }

    // create product
    function create(){

      // query to insert record
      $query = "INSERT INTO
                  " . $this->table_name . "
              SET
                  area=:area, duracao=:duracao, trackingId=:trackingId";

      // prepare query
      $stmt = $this->conn->prepare($query);

      // sanitize
      $this->area=htmlspecialchars(strip_tags($this->area));
      $this->duracao=htmlspecialchars(strip_tags($this->duracao));
      $this->trackingId=htmlspecialchars(strip_tags($this->trackingId));

      // bind values
      $stmt->bindParam(":area", $this->area);
      $stmt->bindParam(":duracao", $this->duracao);
      $stmt->bindParam(":trackingId", $this->trackingId);

      // execute query
      try {
        if($stmt->execute()){
            return true;
        }
      } catch(PDOException $e) {
      }

      return false;

    }
}
