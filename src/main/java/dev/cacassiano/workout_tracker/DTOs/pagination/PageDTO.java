package dev.cacassiano.workout_tracker.DTOs.pagination;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageDTO<DTO>{
    Sort sort;
    Integer page_number;
    Integer page_size;
    Integer page_offset;
    Integer number_of_elements;
    Collection<DTO> data;

    public PageDTO(Page<?> page, Collection<DTO> data){
        this.sort = page.getSort();
        this.page_number = page.getNumber();
        this.page_size = page.getSize();
        this.page_offset = (page_number*page_size)+1;
        this.number_of_elements = page.getNumberOfElements();
        this.data = data;
    }
}
