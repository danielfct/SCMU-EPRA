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

        // select all query
        $query = "SELECT * FROM tracking WHERE id:=id";

        // sanitize
        $this->id=htmlspecialchars(strip_tags($this->id));

        // bind values
        $stmt->bindParam(":id", $this->id);

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
                    id=:id, areaAtual:areaAtual, areaEntrada:areaEntrada, pessoasNotificadas:pessoasNotificadas";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->id=htmlspecialchars(strip_tags($this->id));
        $this->areaAtual=htmlspecialchars(strip_tags($this->areaAtual));
        $this->areaEntrada=htmlspecialchars(strip_tags($this->areaEntrada));
        $this->pessoasNotificadas=htmlspecialchars(strip_tags($this->pessoasNotificadas));

        // bind values
        $stmt->bindParam(":id", $this->id);
        $stmt->bindParam(":areaAtual", $this->areaAtual);
        $stmt->bindParam(":areaEntrada", $this->areaEntrada);
        $stmt->bindParam(":pessoasNotificadas", $this->pessoasNotificadas);

        // execute query
        if($stmt->execute()){
            return true;
        }

        return false;

    }
}
