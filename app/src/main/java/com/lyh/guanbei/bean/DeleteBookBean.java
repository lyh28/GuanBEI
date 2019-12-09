package com.lyh.guanbei.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DeleteBookBean {
    @Id
    private Long book_id;

    @Generated(hash = 202373414)
    public DeleteBookBean(Long book_id) {
        this.book_id = book_id;
    }
    @Generated(hash = 794617505)
    public DeleteBookBean() {
    }

    public static List<DeleteBookBean> createDeleteBookBean(List<Long> idList){
        List<DeleteBookBean> list=new ArrayList<>();
        for(Long id:idList)
            list.add(new DeleteBookBean(id));
        return list;
    }
    public static DeleteBookBean createDeleteBookBean(long id){
        return new DeleteBookBean(id);
    }
    public Long getBook_id() {
        return this.book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }
    public void setBook_id(Long book_id) {
        this.book_id = book_id;
    }
    
}
