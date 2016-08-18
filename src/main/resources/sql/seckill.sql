-- ��ɱִ�д洢����
delimiter $$ -- console ';'ת��Ϊ'$$'
-- ����洢����
-- ����: in �������; out �������
-- row_count():������һ���޸�����sql(delete,insert,update)��Ӱ������
-- row_count(): 0:δ�޸����� >0 �޸����� <0sql�����δִ��
create procedure `seckill`.`execute_seckill`
(in v_seckill_id bigint,in v_phone bigint,
in v_kill_time timestamp,out r_result int)
begin
	declare insert_count int default 0;
	start transaction;
	
	insert ignore into success_killed
		(seckill_id,user_phone,create_time)
		values (v_seckill_id,v_phone,v_kill_time);
	
	select row_count() into insert_count;
	if (insert_count = 0) then
		rollback;
		set r_result = -1;
	elseif(insert_count<0) then
		rollback;
		set r_result = -2;
	else
		update seckill
		set number = number -1
		where seckill_id = v_seckill_id
		and end_time > v_kill_time
		and start_time < v_kill_time
		and number >0;
		
		select row_count() into insert_count;
		
		if(insert_count  = 0 ) then
			rollback;
		set r_result=0;
		elseif (insert_count < 0) then
			rollback;
			set r_result = -2;
		else
			commit;
			set r_result = 1;
		end if;
	end if;
end; 
$$
-- �洢���̶������

delimiter ;
set @r_result = -3;
-- ִ�в��Թ���
call execute_seckill(1003,11111111111,now(),@r_result);
-- ��ȡ���
select @r_result;

--�洢����
--�洢�����Ż�:	1:�����м���������ʱ��
--		   	2:��Ҫ���������洢����,����ҵʹ�ö�,��������˾ʹ�ý���		
--		   	3:�򵥵��߼�,����Ӧ�ô洢����	
--			4:qps:һ����ɱ��6000/qps		