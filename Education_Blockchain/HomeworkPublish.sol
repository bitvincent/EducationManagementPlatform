pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./Table.sol";

/*
    关于作业发布表的操作
    1、创建（管理员）
    2、插入（传入数据）
    3、查询（根据课程id查询课程发布的作业详情列表）
    4、查询（根据课程id和作业次数查询发布的作业详情）
*/
contract HomeworkPublish{
    address private _owner;
    string private _key;
    string private _tablename;
    string[] _field;
    uint _field_length;

    constructor() public{
        _owner = msg.sender;
        _key = "homeworkPublish";
        _tablename = "homework_publish";
        _field_length = 7;
        _field = new string[](_field_length);
        _field[0] = "classid";
        _field[1] = "classname";
        _field[2] = "homeworknumber";
        _field[3] = "homeworkname";
        _field[4] = "description";
        _field[5] = "starttime";
        _field[6] = "ddl";
        TableFactory tf = TableFactory(0x1001);
        tf.createTable(_tablename,"key",
        "classid,classname,homeworknumber,homeworkname,description,starttime,ddl");
    }

    modifier onlyOwner{
        require(_owner == msg.sender,"Auth: only owner is authorized");
        _;
    }
    
    //检查是否已经有记录 根据课程id 和作业次数
    function checkRecordExist(string classid,string homeworknumber) public view returns(int){
        TableFactory tf= TableFactory(0x1001);
        Table table = tf.openTable(_tablename);
        Condition condition = table.newCondition();
        condition.EQ("key",_key);
        condition.EQ("classid",classid);
        condition.EQ("homeworknumber",homeworknumber);
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
    //3、查询（根据课程id查询课程发布的作业详情列表）
    function selectByClassid(string classid) public view returns(string[] memory){
        TableFactory tf= TableFactory(0x1001);
        Table table = tf.openTable(_tablename);
        
        Condition condition = table.newCondition();
        condition.EQ("key",_key);
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

    //4、查询（根据课程id和作业次数查询发布的作业详情）
    function selectByClassidAndNumber(string classid,string homeworknumber) public view returns(string[] memory){
        TableFactory tf= TableFactory(0x1001);
        Table table = tf.openTable(_tablename);
        
        Condition condition = table.newCondition();
        condition.EQ("key",_key);
        condition.EQ("classid",classid);
        condition.EQ("homeworknumber",homeworknumber);

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