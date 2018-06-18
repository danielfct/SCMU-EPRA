<?php
class HistoryTracking {

    // database connection and table name
    private $conn;
    private $table_name = "historicoTracking";

    // object properties
    public $area;
    public $duracao;
    public $trackingId;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read history
    function read($id){
         if ($id == NULL) {
          $query = "SELECT * FROM $this->table_name INNER JOIN areas on $this->table_name.area = areas.id";
        } else {
          $query = "SELECT * FROM $this->table_name INNER JOIN areas on $this->table_name.area = areas.id WHERE trackingId = :trackingId";
        }
        // select all query
        

        // prepare query statement
        $stmt = $this->conn->prepare($query);

       

        // bind values
        if ($id != NULL) {
            $id = htmlspecialchars(strip_tags($id));
            $stmt->bindParam(":trackingId", $id);
        }

        // execute query
        $stmt->execute();

        return $stmt;
    }

    // create product
    function create(){

      // query to insert record
      $query = "INSERT INTO $this->table_name SET
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
        return $stmt->execute();
      } catch(PDOException $e) {
      }

      return false;

    }
}
