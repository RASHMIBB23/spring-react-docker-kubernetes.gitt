export const useUserEmail = () => {
    const email = localStorage.getItem('userEmail' || sessionStorage.getItem('userEmail'));
    return email;
};