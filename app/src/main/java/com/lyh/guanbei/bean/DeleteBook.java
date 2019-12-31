package com.lyh.guanbei.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DeleteBook {
    @Id
    private Long book_id;

    @Generated(hash = 1060262545)
    public DeleteBook(Long book_id) {
        this.book_id = book_id;
    }
    @Generated(hash = 414505498)
    public DeleteBook() {
    }

    public static List<DeleteBook> createDeleteBookBean(List<Long> idList){
        List<DeleteBook> list=new ArrayList<>();
        for(Long id:idList)
            list.add(new DeleteBook(id));
        return list;
    }
    public static DeleteBook createDeleteBookBean(long id){
        return new DeleteBook(id);
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
