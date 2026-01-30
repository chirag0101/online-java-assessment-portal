export interface AdminDetail{
    adminFullName:string,
    adminId:string
}

export interface AdminDetailsResponse{
    status:boolean,
    statusMessage:string,
    response:AdminDetail[]
}