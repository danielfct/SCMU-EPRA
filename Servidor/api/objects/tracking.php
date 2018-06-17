<?php
class Tracking {

    // database connection and table name
    private $conn;
    private $table_name = "tracking";

    // object properties
    public $id;
    public $areaAtual;
    public $areaEntrada;
    public $pessoasNotificadas;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read simulators
    function read(){
      $query = "SELECT tracking.id as tid, a1.nome as 'entrada', areas.nome as 'atual', pessoasNotificadas FROM tracking inner join areas as a1 on tracking.areaEntrada = a1.id inner join areas on tracking.areaAtual = areas.id WHERE tracking.id=$this->id";

      // prepare query statement
      $stmt = $this->conn->prepare($query);

      // execute query
      $stmt->execute();

      return $stmt;
    }

    // create simulator
    function create(){

      // query to insert record
      $query = "INSERT INTO
                  " . $this->table_name . "
              SET
                  areaAtual=:areaAtual, areaEntrada=:areaEntrada, pessoasNotificadas=:pessoasNotificadas";

      // prepare query
      $stmt = $this->conn->prepare($query);

      // sanitize
      $this->areaAtual=htmlspecialchars(strip_tags($this->areaAtual));
      $this->areaEntrada=htmlspecialchars(strip_tags($this->areaEntrada));
      $this->pessoasNotificadas=htmlspecialchars(strip_tags($this->pessoasNotificadas));

      // bind values
      $stmt->bindParam(":areaAtual", $this->areaAtual);
      $stmt->bindParam(":areaEntrada", $this->areaEntrada);
      $stmt->bindParam(":pessoasNotificadas", $this->pessoasNotificadas);

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
