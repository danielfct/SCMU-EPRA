<?php
class SensorsActuators {

    // database connection and table name
    private $conn;
    private $table_name = "sensoresAtuadores";

    // object properties
    public $nome;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read history
    function read(){

        // select all query
        $query = "SELECT * FROM sensoresAtuadores";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

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
                    nome=:nome";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->nome=htmlspecialchars(strip_tags($this->nome));

        // bind values
        $stmt->bindParam(":nome", $this->nome);

        // execute query
        if($stmt->execute()){
            return true;
        }

        return false;

    }
}
