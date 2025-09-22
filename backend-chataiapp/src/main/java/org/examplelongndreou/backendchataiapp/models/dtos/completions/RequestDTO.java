package org.examplelongndreou.backendchataiapp.models.dtos.completions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    private String model;
    private List<MessageDTO> messages;



}