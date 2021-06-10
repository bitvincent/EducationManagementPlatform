pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./Table.sol";

/*
    关于学生表的操作
    1、创建（管理员）
    2、插入（传入数据，studentid需唯一）
    3、查询（根据studentid查询信息）
*/
contract StuInfo{
    address private _owner;
    string private _key;
    string private _tablename;
    string[] _field;
    uint _field_length;

    constructor() public{
        _owner = msg.sender;
        _key = "stuinfo";
        _tablename = "stu_info2";
        _field_length = 11;
        _field = new string[](_field_length);
        _field[0] = "studentid";
        _field[1] = "studentname";
        _field[2] = "gender";
        _field[3] = "idcard";
        _field[4] = "schoolspecial";
        _field[5] = "institute";
        _field[6] = "major";
        _field[7] = "sno";
        _field[8] = "intime";
        _field[9] = "outtime";
        _field[10] = "degree";
        TableFactory tf = TableFactory(0x1001);
        tf.createTable(_tablename,"key",
        "studentid,studentname,gender,idcard,schoolspecial,institute,major,sno,intime,outtime,degree");
    }

    modifier onlyOwner{
        require(_owner == msg.sender,"Auth: only owner is authorized");
        _;
    }
    
    
    //检查是否已经有记录
    function checkRecordExist(string studentid) public view returns(int){
        TableFactory tf= TableFactory(0x1001);
        Table table = tf.openTable(_tablename);
        Condition condition = table.newCondition();
        condition.EQ("key",_key);
        condition.EQ("studentid",studentid);
        Entries entries = table.select(_key,condition);
        return entries.size();
    }
    //插入信息
    // 0 失败 1 成功
    function insert(string[] info) public returns(int){
        uint length = info.length;
        //参数长度不匹配
        if(length!=_field_length) return 0;
        //检查是否已经有记录
        if(checkRecordExist(info[0])>0) return 0;
        //插入数据
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable(_tablename);
        Entry entry = table.newEntry();
        for(uint i =0;i<_field_length;++i){
            entry.set(_field[i],info[i]);
        }
        entry.set("key",_key);
        return table.insert(_key,entry);
    }

    //查询信息
    function selectByStudentid(string studentid) public view returns(string[] memory){
        TableFactory tf= TableFactory(0x1001);
        Table table = tf.openTable(_tablename);
        
        Condition condition = table.newCondition();
        condition.EQ("key",_key);
        condition.EQ("studentid",studentid);

        Entries entries = table.select(_key,condition);
        string[] memory res = new string[](_field_length);
        if(entries.size()==0) return res;
        Entry entry = entries.get(0);
        for(uint i=0;i<_field_length;++i){
            res[i] = entry.getString(_field[i]);
        }
        return res;
    }
}