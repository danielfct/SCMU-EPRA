<?php
class Device {

    // database connection and table name
    private $conn;
    private $table_name;

    // object properties
    public $nome;
    public $tipo;
    public $ligado;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
        $this->table_name = "devices";
    }

    // read history
    function read() {

        // select all query
        $query = "SELECT * FROM $this->table_name";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // execute query
        $stmt->execute();

        return $stmt;
    }

    // create product
    function create(){

        // query to insert record
        $query = "INSERT INTO $this->table_name SET
            nome=:nome,tipo=:tipo,ligado=:ligado";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->nome=htmlspecialchars(strip_tags($this->nome));
        $this->tipo=htmlspecialchars(strip_tags($this->tipo));
        $this->ligado=htmlspecialchars(strip_tags($this->ligado));

        // bind values
        $stmt->bindParam(":nome", $this->nome);
        $stmt->bindParam(":tipo", $this->tipo);
        $stmt->bindParam(":ligado", $this->ligado);

        // execute query
       return $stmt->execute();
    }


    function update() {
      
      $query = "UPDATE
                  " . $this->table_name . "
              SET
                  ligado=:ligado
              WHERE
                  nome = :nome";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->nome=htmlspecialchars(strip_tags($this->nome));
        $this->ligado=htmlspecialchars(strip_tags($this->ligado));

        // bind new values
        $stmt->bindParam(":nome", $this->nome);
        $stmt->bindParam(":ligado", $this->ligado);

        // execute the query
        try {
          if($stmt->execute()){
              return true;
          }
        } catch(PDOException $e) {
        }

        return false;
    }

}
