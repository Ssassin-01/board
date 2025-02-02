import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/axiosInstance';
import '../../style/PostStyles.css';

const PostListPage = () => {
  const [posts, setPosts] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await api.get('/posts');
        setPosts(response.data);
      } catch (error) {
        alert('게시물 불러오기 실패');
      }
    };
    fetchPosts();
  }, []);

  return (
    <div className="feed-container">
      <h2 className="feed-title">📢 피드</h2>
      <button className="feed-btn" onClick={() => navigate('/posts/create')}>
        새 글 작성
      </button>
      <div className="feed-list">
        {posts.map((post) => (
          <div
            key={post.id}
            className="feed-card"
            onClick={() => navigate(`/posts/${post.id}`)}
          >
            <div className="feed-header">
              <img
                src="/images/default-profile.png"
                alt="프로필"
                className="profile-img"
              />
              <h3>{post.author}</h3>
            </div>
            <p className="feed-content">{post.content}</p>
            <div className="feed-actions">
              <button className="like-btn">👍 좋아요</button>
              <button className="comment-btn">💬 댓글</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default PostListPage;
