import api from './axiosInstance';

// ✅ 프로필 이미지 업로드 (fetch ❌ → axios ✅)
export const uploadProfileImage = async (file) => {
  const formData = new FormData();
  formData.append('file', file);

  const response = await api.post('/members/profile-image', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });

  return response.data.profileImageURL;
};

// ✅ 프로필 이미지 삭제
export const deleteProfileImage = async () => {
  await api.delete('/members/profile-image');
};
// ✅ 프로필 이미지 URL 변환
export const getProfileImage = (profileImageURL) => {
  if (!profileImageURL) {
    return `${process.env.REACT_APP_API_BASE_URL}/api/members/profile-image/default-profile.png`; // ✅ 백엔드에서 기본 이미지 반환
  }

  if (!profileImageURL.startsWith('http')) {
    return `${process.env.REACT_APP_API_BASE_URL}${profileImageURL}`;
  }

  return profileImageURL;
};
