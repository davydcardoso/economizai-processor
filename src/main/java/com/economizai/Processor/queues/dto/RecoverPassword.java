package com.economizai.Processor.Queues.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecoverPassword {
    private String email;
    private String link;
}
