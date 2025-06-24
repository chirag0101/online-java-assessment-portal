export interface AdminDetails {
  adminId: string;
  isAdmin:boolean;
  adminFullName:string;
  accessToken:string;
}

export interface AdminLoginResponse{
    status:boolean,
    statusMessage:string,
    response:AdminDetails
}