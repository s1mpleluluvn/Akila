/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author quangminh
 */
@Getter
@Setter
@AllArgsConstructor
public class LoggerJson {

    private String userName;
    private String uuid;
    private String mode;//input or output
    private String api;
    private String ipAddress;
    private Object DATA;//request data
    private Object dataParams;
    private String className;//class
    private Long timeExec;//time execute api
}
