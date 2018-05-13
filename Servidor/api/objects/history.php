<?php
class History {

    // database connection and table name
    private $conn;
    private $table_name = "historico";

    // object properties
    public $id;
    public $evento;
    public $datahora;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read history
    function read(){

        // select all query
        $query = "SELECT * FROM historico";

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
                    evento=:evento";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->evento=htmlspecialchars(strip_tags($this->evento));

        // bind values
        $stmt->bindParam(":evento", $this->evento);

        // execute query
        if($stmt->execute()){
            return true;
        }

        return false;

    }
}
