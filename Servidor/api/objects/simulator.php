<?php
class Simulator {

    // database connection and table name
    private $conn;
    private $table_name = "simuladores";

    // object properties
    public $id;
    public $nome;
    public $estadoAtual;
    public $atuador;
    public $areaId;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read simulators
    function read(){

        // select all query
        $query = "SELECT * FROM simuladores";

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
                    id=:id, nome:nome, estadoAtual:estadoAtual, atuador:atuador, areaId:areaId";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->id=htmlspecialchars(strip_tags($this->id));
        $this->nome=htmlspecialchars(strip_tags($this->nome));
        $this->estadoAtual=htmlspecialchars(strip_tags($this->estadoAtual));
        $this->atuador=htmlspecialchars(strip_tags($this->atuador));
        $this->areaId=htmlspecialchars(strip_tags($this->areaId));

        // bind values
        $stmt->bindParam(":id", $this->id);
        $stmt->bindParam(":nome", $this->nome);
        $stmt->bindParam(":estadoAtual", $this->estadoAtual);
        $stmt->bindParam(":atuador", $this->atuador);
        $stmt->bindParam(":areaId", $this->areaId);

        // execute query
        if($stmt->execute()){
            return true;
        }

        return false;

    }
}
