import React, { useEffect, useState } from 'react';
import api from '../api/axiosInstance'; // ✅ Axios 유지
import PostCard from '../components/PostCard';
import '../style/FeedStyles.css';

const FeedPage = () => {
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await api.get('/posts');
        setPosts(response.data);
      } catch (error) {
        alert('게시글을 불러오는 데 실패했습니다.');
      }
    };
    fetchPosts();
  }, []);

  return (
    <div className="feed-container">
      <h2 className="feed-title">📢 소셜 피드</h2>
      <div className="feed-list">
        {posts.map((post) => (
          <PostCard key={post.id} post={post} />
        ))}
      </div>
    </div>
  );
};

export default FeedPage;
