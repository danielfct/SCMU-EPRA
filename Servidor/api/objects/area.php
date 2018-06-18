<?php
class Area {

    // database connection and table name
    private $conn;
    private $table_name = "areas";

    // object properties
    public $id;
    public $nome;
    public $alarmeLigado;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read simulators
    function read($id){

        // select all query
        if ($id == NULL) {
          $query = "SELECT * FROM areas";
        } else {
          $query = "SELECT * FROM areas WHERE id=$id";
        }

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
                    nome=:nome, alarmeLigado=:alarmeLigado";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->nome=htmlspecialchars(strip_tags($this->nome));
        $this->alarmeLigado=htmlspecialchars(strip_tags($this->alarmeLigado));

        // bind values
        $stmt->bindParam(":nome", $this->nome);
        $stmt->bindParam(":alarmeLigado", $this->alarmeLigado);

        // execute query
        try {
          if($stmt->execute()){
              return true;
          }
        } catch(PDOException $e) {
        }

        return false;

    }

    // update area
    function update(){

      $query = "UPDATE
                  " . $this->table_name . "
              SET
                  nome = :nome,
                  alarmeLigado = :alarmeLigado
              WHERE
                  id = :id";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->id=htmlspecialchars(strip_tags($this->id));
        $this->nome=htmlspecialchars(strip_tags($this->nome));
        $this->alarmeLigado=htmlspecialchars(strip_tags($this->alarmeLigado));

        // bind new values
        $stmt->bindParam(':id', $this->id);
        $stmt->bindParam(':nome', $this->nome);
        $stmt->bindParam(':alarmeLigado', $this->alarmeLigado);

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
        $query = "DELETE FROM $this->table_name WHERE id = ?";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->id=htmlspecialchars(strip_tags($this->id));

        // bind id of record to delete
        $stmt->bindParam(1, $this->id);

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
