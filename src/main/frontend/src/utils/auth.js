export const getCookie = (name) => {
  const matches = document.cookie.match(
    new RegExp('(^| )' + name + '=([^;]+)')
  );
  return matches ? matches[2] : null;
};

export const isAuthenticated = () => {
  return !!getCookie('accessToken'); // ✅ accessToken 쿠키가 존재하면 로그인 상태
};

export const removeCookie = (name) => {
  document.cookie = `${name}=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT`;
};
