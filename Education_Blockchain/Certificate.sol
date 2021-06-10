pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./Table.sol";

/*
    关于证书表的操作
    1、创建（管理员）
    2、插入（传入数据，学生id、课程id作为唯一对）
    3、查询（根据学生id查询课程成绩列表）
    4、查询（根据学生id、课程id查询课程成绩详情）
    5、查询（根据教师id、课程id查询该课程下面的学生课程成绩列表）
*/
contract Certificate{
    address private _owner;
    string private _key;
    string private _tablename;
    string[] _field;
    uint _field_length;

    constructor() public{
        _owner = msg.sender;
        _key = "certification";
        _tablename = "certificate";
        _field_length = 5;
        _field = new string[](_field_length);
        _field[0] = "studentid";
        _field[1] = "teacherid";
        _field[2] = "classid";
        _field[3] = "passtime";
        _field[4] = "finalgrade";
        //创建表
        TableFactory tf = TableFactory(0x1001);
        tf.createTable(_tablename,"key",
        "studentid,teacherid,classid,passtime,finalgrade");
    }

    modifier onlyOwner{
        require(_owner == msg.sender,"Auth: only owner is authorized");
        _;
    }
    
    //检查是否已经有记录 根据学生id 和 课程id
    function checkRecordExist(string studentid,string classid) public view returns(int){
        TableFactory tf= TableFactory(0x1001);
        Table table = tf.openTable(_tablename);
        Condition condition = table.newCondition();
        condition.EQ("key",_key);
        condition.EQ("studentid",studentid);
        condition.EQ("classid",classid);
        Entries entries = table.select(_key,condition);
        return entries.size();
    }
    
    //插入信息
    // 0 失败 1 成功
    function insert(string[] info) public returns(int){
        uint length = info.length;
        //参数长度不匹配
        if(length!=_field_length) return 0;
        // //检查是否已经有记录
        if(checkRecordExist(info[0],info[2])>0) return 0;
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
    //3、查询（根据学生id查询课程成绩列表）
    function selectByStudentid(string studentid) public view returns(string[] memory){
        TableFactory tf= TableFactory(0x1001);
        Table table = tf.openTable(_tablename);
        
        Condition condition = table.newCondition();
        condition.EQ("key",_key);
        condition.EQ("studentid",studentid);

        Entries entries = table.select(_key,condition);
        uint size = uint256(entries.size());
        string[] memory res = new string[](_field_length*size);
        if(entries.size()==0) return res;
        for(uint j=0;j<size;++j){
            int index = int(j);
            Entry entry = entries.get(index);
            for(uint i=0;i<_field_length;++i){
                res[_field_length*j+i] = entry.getString(_field[i]);
            }
        }
        return res;
    }

    //4、查询（根据学生id、课程id查询课程成绩详情）
    function selectByStudentidAndClassid(string studentid,string classid) public view returns(string[] memory){
        TableFactory tf= TableFactory(0x1001);
        Table table = tf.openTable(_tablename);
        
        Condition condition = table.newCondition();
        condition.EQ("key",_key);
        condition.EQ("studentid",studentid);
        condition.EQ("classid",classid);

        Entries entries = table.select(_key,condition);
        string[] memory res = new string[](_field_length);
        if(entries.size()==0) return res;
        Entry entry = entries.get(0);
        for(uint i=0;i<_field_length;++i){
            res[i] = entry.getString(_field[i]);
        }
        return res;
    }

    //5、查询（根据教师id、课程id查询该课程下面的学生课程成绩列表）
    function selectByTeacheridAndClassid(string teacherid,string classid) public view returns(string[] memory){
        TableFactory tf= TableFactory(0x1001);
        Table table = tf.openTable(_tablename);
        
        Condition condition = table.newCondition();
        condition.EQ("key",_key);
        condition.EQ("teacherid",teacherid);
        condition.EQ("classid",classid);

        Entries entries = table.select(_key,condition);
        uint size = uint256(entries.size());
        string[] memory res = new string[](_field_length*size);
        if(entries.size()==0) return res;
        for(uint j=0;j<size;++j){
            int index = int(j);
            Entry entry = entries.get(index);
            for(uint i=0;i<_field_length;++i){
                res[_field_length*j+i] = entry.getString(_field[i]);
            }
        }
        return res;
    }
}