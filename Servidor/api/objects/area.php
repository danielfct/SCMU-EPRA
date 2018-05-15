<?php
class Area {

    // database connection and table name
    private $conn;
    private $table_name = "areas";

    // object properties
    public $id;
    public $nome;
    public $alarmeLigado;
    public $sensor;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read simulators
    function read(){

        // select all query
        $query = "SELECT * FROM areas";

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
                    id=:id, nome:nome, alarmeLigado:alarmeLigado, sensor:sensor";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->id=htmlspecialchars(strip_tags($this->id));
        $this->nome=htmlspecialchars(strip_tags($this->nome));
        $this->alarmeLigado=htmlspecialchars(strip_tags($this->alarmeLigado));
        $this->sensor=htmlspecialchars(strip_tags($this->sensor));

        // bind values
        $stmt->bindParam(":id", $this->id);
        $stmt->bindParam(":nome", $this->nome);
        $stmt->bindParam(":alarmeLigado", $this->alarmeLigado);
        $stmt->bindParam(":sensor", $this->sensor);

        // execute query
        if($stmt->execute()){
            return true;
        }

        return false;

    }
}
