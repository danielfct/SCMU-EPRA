<?php
class Device {

    // database connection and table name
    private $conn;
    private $table_name;

    // object properties
    public $nome;
    public $tipo;
    public $ligado;
    public $areaId;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
        $this->table_name = "devices";
    }

    // read 
    function read($areaId) {
        $query = "SELECT * FROM $this->table_name";
        if (isset($areaId)) {
            $areaId = htmlspecialchars(strip_tags($areaId));
            $query .= " WHERE areaId =:areaId";
        }
        
        // prepare query statement
        $stmt = $this->conn->prepare($query);
        if (isset($areaId)) {
            $stmt->bindParam(":areaId", $areaId);
        }    
    
        // execute query
        $stmt->execute();

        return $stmt;
    }

    // create product
    function create() {

        // query to insert record
        $query = "INSERT INTO $this->table_name SET
            nome=:nome,tipo=:tipo,ligado=:ligado,areaId=:areaId";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->nome=htmlspecialchars(strip_tags($this->nome));
        $this->tipo=htmlspecialchars(strip_tags($this->tipo));
        $this->ligado=htmlspecialchars(strip_tags($this->ligado));
        $this->areaId=htmlspecialchars(strip_tags($this->areaId));

        // bind values
        $stmt->bindParam(":nome", $this->nome);
        $stmt->bindParam(":tipo", $this->tipo);
        $stmt->bindParam(":ligado", $this->ligado);
        $stmt->bindParam(":areaId", $this->areaId);

        // execute query
       return $stmt->execute();
    }

function buildQueryAttributes() {
      $res = "";
      if(isset($this->nome)) {
        $res .= "nome = :nome";
      }
      if(isset($this->tipo)) {
        $res = $this->addComma($res);
        $res .= "tipo = :tipo";
      }
      if(isset($this->ligado)) {
        $res = $this->addComma($res);
        $res .= "ligado = :ligado";
      }
      if(isset($this->areaId)) {
        $res = $this->addComma($res);
        $res .= "areaId = :areaId";
      }

      return $res;
    }

    function addComma($res) {
      if(strlen($res) > 0) {
        $res .= ", ";
      }
      return $res;
    }

    function update() {
      
      $query = "UPDATE $this->table_name SET " 
                . $this->buildQueryAttributes() .
                " WHERE nome = :nome";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->nome=htmlspecialchars(strip_tags($this->nome));
        $this->ligado=htmlspecialchars(strip_tags($this->ligado));
        if (isset($this->tipo)) {
            $this->tipo=htmlspecialchars(strip_tags($this->tipo));
        }
        if (isset($this->areaId)) {
            $this->areaId=htmlspecialchars(strip_tags($this->areaId));
        }


        // bind new values
        $stmt->bindParam(":nome", $this->nome);
        $stmt->bindParam(":ligado", $this->ligado);
        if (isset($this->tipo)) {
            $stmt->bindParam(":tipo", $this->tipo);
        }
        if (isset($this->areaId)) {
            $stmt->bindParam(":areaId", $this->areaId);
        }


        // execute the query
        try {
          if($stmt->execute()){
              return true;
          }
        } catch(PDOException $e) {
        }

        return false;
    }
    
        function delete() {

        // delete query
        $query = "DELETE FROM $this->table_name WHERE nome = ?";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->nome=htmlspecialchars(strip_tags($this->nome));

        // bind id of record to delete
        $stmt->bindParam(1, $this->nome);

        try {
          // execute query
          if($stmt->execute()){
              return true;
          }
        } catch(PDOException $e) {
        }

        return false;

    }

}
