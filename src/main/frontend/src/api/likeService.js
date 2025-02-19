import api from './axiosInstance';

// ✅ 좋아요 추가
export const likePost = async (postId) => {
  return api.post(`/posts/${postId}/likes`);
};

// ✅ 좋아요 취소
export const unlikePost = async (postId) => {
  return api.delete(`/posts/${postId}/likes`);
};

// ✅ 좋아요 여부 확인
export const checkLikedStatus = async (postId) => {
  const response = await api.get(`/posts/${postId}/liked`);
  return response.data.liked;
};

// ✅ 좋아요 개수 조회
export const getLikeCount = async (postId) => {
  const response = await api.get(`/posts/${postId}/likes/counts`);
  return response.data.likes;
};
